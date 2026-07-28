-- KEYS[1] = session hash key         (session:{sessionId})
-- KEYS[2] = inventory hash key       (inventory:{ticketTypeId})
-- KEYS[3] = inventory available key  (inventory:available:{ticketTypeId})
-- ARGV[1] = quantity muốn giữ/mua
-- ARGV[2] = current epoch seconds (truyền từ Java)

-- Return codes:
--  1  = success
--  0  = insufficient stock
-- -1  = ticket type not found in cache (not warmed up or expired)
-- -3  = session not found in cache
-- -4  = session is not in PUBLISHED status
-- -5  = outside the sales window (before salesStartAt or after salesEndAt)

local sessionKey = KEYS[1]
local invKey = KEYS[2]
local availKey = KEYS[3]
local qty = tonumber(ARGV[1])
local now = tonumber(ARGV[2])

if redis.call('EXISTS', sessionKey) == 0 then
    return -3
end

local sessionStatus = redis.call('HGET', sessionKey, 'status')
if sessionStatus ~= 'PUBLISHED' then
    return -4
end

local salesStartAt = tonumber(redis.call('HGET', sessionKey, 'salesStartAtEpoch'))
local salesEndAt = tonumber(redis.call('HGET', sessionKey, 'salesEndAtEpoch'))

if salesStartAt == nil or salesEndAt == nil then
    return -3
end

if now < salesStartAt or now > salesEndAt then
    return -5
end

if redis.call('EXISTS', invKey) == 0 then
    return -1
end

local available = tonumber(redis.call('GET', availKey))
if available == nil then
    return -1
end

if available < qty then
    return 0
end

redis.call('DECRBY', availKey, qty)
redis.call('HINCRBY', invKey, 'reservedStock', qty)

return 1