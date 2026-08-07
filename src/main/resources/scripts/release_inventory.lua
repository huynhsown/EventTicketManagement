-- KEYS[1] = inventory hash key
-- KEYS[2] = inventory available key
-- ARGV[1] = quantity

local invKey = KEYS[1]
local availKey = KEYS[2]
local qty = tonumber(ARGV[1])

if redis.call('EXISTS', invKey) == 0 then
    return -1
end

local reserved = tonumber(redis.call('HGET', invKey, 'reservedStock')) or 0

if reserved < qty then
    return -2
end

redis.call('INCRBY', availKey, qty)
redis.call('HINCRBY', invKey, 'reservedStock', -qty)

return 1