package com.xiaored.vo;

import com.xiaored.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuTreeVo {
    private Long id;
    private String label;
    //因为前端想要的是label，我又不会改前端，那就只能先bean拷贝得到menuName，然后用set方法给到label
    private String menuName;
    private Long parentId;
    private List<MenuTreeVo> children;
}
