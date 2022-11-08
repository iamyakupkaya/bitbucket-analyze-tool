package com.orion.bitbucket.entity.pull_request.common;

import com.orion.bitbucket.entity.common.CommonLinksEntity;

/* this class is used in values/links and values/fromRef/repository/project/links
   and values/toRef/repository/project/links and values/author/user/links
   and values/reviewers/user/links as a common links class. this link just has self key.

   !!Important, don't confuse this with values/fromRef/repository/links and values/toRef/repository/links
   they are different. values/fromRef/repository/links path has clone and self keys!
 */
public class PRLinksEntity extends CommonLinksEntity {
    public PRLinksEntity(){};
}
