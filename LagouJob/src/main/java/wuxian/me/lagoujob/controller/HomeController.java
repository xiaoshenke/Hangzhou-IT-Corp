package wuxian.me.lagoujob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wuxian on 1/5/2017.
 */
@Controller
public class HomeController {

    static {
        System.out.println("HomeController");
    }

    @RequestMapping(value = {"/home"})
    @ResponseBody
    public String index() {
        System.out.println("home");
        return "index";
    }
}
