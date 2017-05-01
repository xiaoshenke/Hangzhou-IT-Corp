package wuxian.me.lagoujob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wuxian on 1/5/2017.
 */
@Controller
public class HomeController {

    @RequestMapping(value = {"/home"})
    public String index() {
        return "index";
    }
}
