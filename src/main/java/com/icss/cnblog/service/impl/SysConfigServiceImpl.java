package com.icss.cnblog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.entity.SysConfigEntity;
import com.icss.cnblog.mapper.SysConfigMapper;
import com.icss.cnblog.service.SysConfigService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements SysConfigService {


    @Override
    public PageUtils queryPage(Query query) {
        String keywords = query.getKeywords();
        Page<SysConfigEntity> page = this.selectPage(
                query.getPagePlus(),
                new EntityWrapper<SysConfigEntity>()
                        .like(StringUtils.isNotBlank(keywords),"config_key", keywords)
        );
        return new PageUtils(page);
    }

    @Override
    public void website(Map<String, String> configs) {
        if (!configs.isEmpty() && null != configs) {
            configs.forEach((k, v) -> saveConfig(k, v));
        }
    }

    @Override
    public Map<String, String> selectAll() {
        List<SysConfigEntity> list = super.selectList(new EntityWrapper<SysConfigEntity>());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, String> res = new HashMap<String, String>();
        for (SysConfigEntity config: list) {
            res.put(config.getConfigKey(), config.getConfigValue());
        }
        return res;
    }

    /**
     *  key
     * @param key
     * @return
     */
    private SysConfigEntity getByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        SysConfigEntity sysConfig = new SysConfigEntity();
        sysConfig.setConfigKey(key);
        return this.baseMapper.selectOne(sysConfig);
    }

    /**
     * 保存 Or 修改
     * @param key
     * @param value
     */
    private void saveConfig(String key, String value) {
        if (!StringUtils.isEmpty(key)) {
            SysConfigEntity config = null;
            if (null == (config = this.getByKey(key))) {
                config = new SysConfigEntity();
                config.setConfigKey(key);
                config.setConfigValue(value);
                this.insert(config);
            } else {
                config.setConfigKey(key);
                config.setConfigValue(value);
                this.updateById(config);
            }
        }
    }

}
