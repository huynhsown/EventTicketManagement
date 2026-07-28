-- KEYS[1] = inventory hash key       (inventory:{ticketTypeId})
-- KEYS[2] = inventory available key  (inventory:available:{ticketTypeId})
-- ARGV[1] = totalStock
-- ARGV[2] = reservedStock
-- ARGV[3] = soldStock
-- ARGV[4] = status
-- ARGV[5] = ttlSeconds

local exists = redis.call('EXISTS', KEYS[1])

if exists == 0 then
    redis.call('HSET', KEYS[1],
        'totalStock', ARGV[1],
        'reservedStock', ARGV[2],
        'soldStock', ARGV[3],
        'status', ARGV[4])

    local available = tonumber(ARGV[1]) - tonumber(ARGV[2]) - tonumber(ARGV[3])
    redis.call('SET', KEYS[2], available)
end

redis.call('EXPIRE', KEYS[1], ARGV[5])
redis.call('EXPIRE', KEYS[2], ARGV[5])

return exists