package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AlarmFlag;

public interface AlarmFlagRepository extends  BaseRepository<AlarmFlag, Long> {
    void deleteAllByFlag(String flag);

    AlarmFlag findByFlag(String flag);
}
