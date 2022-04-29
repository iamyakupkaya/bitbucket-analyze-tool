package com.orion.bitbucket.Bitbucket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.orion.bitbucket.Bitbucket.service.InspectionService;

@RestController
public class InspectorController {

    @Autowired
    private InspectionService inspectionService;

    // @RequestMapping("/api/inspectors/{prId}")
    // public String prInspectors(@PathVariable int prId) throws JsonSyntaxException, UnirestException {
    //    return inspectionService.getPRreviewers(prId);
    // }

    // @RequestMapping("/api/inspectors/reviews")
    // public String reviews() throws JsonSyntaxException, UnirestException {
    //    return inspectionService.getReviewCount();
    // }

    // @RequestMapping("/api/inspectors/approvals")
    // public String approvals() throws JsonSyntaxException, UnirestException {
    //    return inspectionService.getApprovalCounts();
    // }

}
