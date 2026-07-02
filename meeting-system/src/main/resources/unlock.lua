-- 删除保持原子性
if(redis.call('get',KEYS[1])== ARGV[1]) then
    return redis.call('del',KEYS[1])
end
return 0