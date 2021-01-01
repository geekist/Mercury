package com.alibaba.android.arouter.routes;

import com.alibaba.android.arouter.facade.enums.RouteType;
import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IRouteGroup;
import com.ytech.knowledge.knowledge.KnowledgeFragment;
import com.ytech.knowledge.knowledgedetail.KnowledgeDetailFragment;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY AROUTER. */
public class ARouter$$Group$$knowledge implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/knowledge/KnowledgeDetailFragment", RouteMeta.build(RouteType.FRAGMENT, KnowledgeDetailFragment.class, "/knowledge/knowledgedetailfragment", "knowledge", new java.util.HashMap<String, Integer>(){{put("title", 8); put("cid", 3); }}, -1, -2147483648));
    atlas.put("/knowledge/KnowledgeFragment", RouteMeta.build(RouteType.FRAGMENT, KnowledgeFragment.class, "/knowledge/knowledgefragment", "knowledge", null, -1, -2147483648));
  }
}
