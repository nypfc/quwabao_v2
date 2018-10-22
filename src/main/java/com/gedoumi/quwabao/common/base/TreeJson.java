package com.gedoumi.quwabao.common.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeJson {

	private String id;
	private String pid;
	private String text;
	private String iconCls;
	private String state;
	private String checked;
	private JSONObject attributes = new JSONObject();
	private List<TreeJson> children = new ArrayList<>();
	
	public static List<TreeJson> formatTree(List<TreeJson> list) {
        TreeJson root = new TreeJson();
        TreeJson node = new TreeJson();
        List<TreeJson> treelist = new ArrayList<TreeJson>();// 拼凑好的json格式的数据
        List<TreeJson> parentnodes = new ArrayList<TreeJson>();// parentnodes存放所有的父节点
        
        if (list != null && list.size() > 0) {
            root = list.get(0) ;
            root.setChecked("true");
            //循环遍历oracle树查询的所有节点
            for (int i = 1; i < list.size(); i++) {
                node = list.get(i);
                if(node.getPid().equals(root.getId())){
                    //为tree root 增加子节点
                    parentnodes.add(node) ;
                    root.getChildren().add(node) ;
                }else{//获取root子节点的孩子节点
                    getChildrenNodes(parentnodes, node);
//                    parentnodes.add(node) ;
                }
            }    
        }
        treelist.add(root) ;
        return treelist ;

    }

    private static void getChildrenNodes(List<TreeJson> parentnodes, TreeJson node) {
        //循环遍历所有父节点和node进行匹配，确定父子关系
        for (int i = parentnodes.size() - 1; i >= 0; i--) {
            TreeJson pnode = parentnodes.get(i);
            //如果是父子关系，为父节点增加子节点，退出for循环
            if (pnode.getId().equals(node.getPid())) {
                pnode.setState("closed") ;//关闭二级树
                pnode.getChildren().add(node) ;
                return ;
            } else {
                //如果不是父子关系，删除父节点栈里当前的节点，
                //继续此次循环，直到确定父子关系或不存在退出for循环
//                parentnodes.remove(i) ;
            }
        }
    }

}
