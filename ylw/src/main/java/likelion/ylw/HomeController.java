package likelion.ylw;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String homeController() {
        return "index";
    }

    @RequestMapping("/layout")
    public String layouttest() {
        return "examples/tables";
    }
}
