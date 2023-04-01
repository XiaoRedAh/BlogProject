package com.xiaored.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaored.domain.ResponseResult;
import com.xiaored.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 21:27:02
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

