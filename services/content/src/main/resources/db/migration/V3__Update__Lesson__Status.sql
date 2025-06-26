UPDATE lessons
SET status = 'UN_SYNC'
WHERE status <> 'SYNCED';
