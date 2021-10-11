package com.icss.cnblog.modules.blog.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.modules.blog.entity.BizCommentEntity;
import com.icss.cnblog.modules.blog.mapper.BizCommentMapper;
import com.icss.cnblog.modules.blog.service.BizCommentService;
import org.springframework.stereotype.Service;


@Service("bizCommentService")
public class BizCommentServiceImpl extends ServiceImpl<BizCommentMapper, BizCommentEntity> implements BizCommentService {
}
