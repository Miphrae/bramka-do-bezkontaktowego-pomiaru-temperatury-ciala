package bramka_do_temperatury.SpringApplication1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
public class AppController implements WebMvcConfigurer {

    @Autowired
    private PomiarDAO dao;
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/main_admin").setViewName("admin/main_admin");
        registry.addViewController("/main_user").setViewName("user/main_user");
    }

    @Controller
    public class DashboardController {
        @RequestMapping("/main")
        public String defaultAfterLogin(HttpServletRequest request) {
            if (request.isUserInRole("ADMIN")) {
                return "redirect:/main_admin";
            }
            else if (request.isUserInRole("USER")) {
                return "redirect:/main_user";
            }
            else {
                return "redirect:/index";
            }
        }

        @RequestMapping("/main_admin")
        public String viewTempRecAdminPage(Model model) {
            List<Pomiar> listPomiar = dao.list();
            model.addAttribute("listPomiar", listPomiar);
            return "admin/temp_record_admin";
        }

        @RequestMapping("/new")
        public String showNewForm(Model model) {
            Pomiar pomiar = new Pomiar();
            pomiar.setCzy_dodane_recznie(true);
            model.addAttribute("pomiar", pomiar);
            return "admin/new_form_admin";
        }

        @RequestMapping(value = "/save", method = RequestMethod.POST)
        public String save(@ModelAttribute("pomiar") Pomiar pomiar) {

            dao.save(pomiar);
            System.out.println(pomiar);
            return "redirect:/main";
        }

        @RequestMapping("/edit/{nr_pomiaru}")
        public ModelAndView showEditForm(@PathVariable(name = "nr_pomiaru") int nr_pomiaru) {
            ModelAndView mav = new ModelAndView("admin/edit_form_admin");
            Pomiar pomiar = dao.get(nr_pomiaru);
            mav.addObject("pomiar", pomiar);
            return mav;

        }

        @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String update(@ModelAttribute("pomiar") Pomiar pomiar) {

            pomiar.setCzy_dodane_recznie(true);
            dao.update(pomiar);

            return "redirect:/main";
        }

        @RequestMapping("/delete/{nr_pomiaru}")
        public String delete(@PathVariable(name = "nr_pomiaru")int nr_pomiaru) {
            dao.delete(nr_pomiaru);
            return "redirect:/main";
        }

        @RequestMapping("/main_user")
        public String viewTempRecUserPage(Model model) {
            List<Pomiar> listPomiar = dao.list();
            model.addAttribute("listPomiar", listPomiar);
            return "user/temp_record_user";
        }


    }

    @Controller
    public class UserDashboardController {

        @RequestMapping("/new-user")
        public String showNewForm(Model model) {
            Pomiar pomiar = new Pomiar();
            model.addAttribute("pomiar", pomiar);
            return "user/new_form_user";
        }

        @RequestMapping(value = "/save-user", method = RequestMethod.POST)
        public String save(@ModelAttribute("pomiar") Pomiar pomiar) {
            pomiar.setCzy_dodane_recznie(true);
            dao.save(pomiar);
            return "redirect:/main";
        }

        @RequestMapping("/edit-user/{nr_pomiaru}")
        public ModelAndView showEditForm(@PathVariable(name = "nr_pomiaru") int nr_pomiaru) {
            ModelAndView mav = new ModelAndView("user/edit_form_user");
            Pomiar pomiar = dao.get(nr_pomiaru);
            mav.addObject("pomiar", pomiar);
            return mav;

        }

        @RequestMapping(value = "/update-user", method = RequestMethod.POST)
        public String update(@ModelAttribute("pomiar") Pomiar pomiar) {

            pomiar.setCzy_dodane_recznie(true);
            dao.update(pomiar);

            return "redirect:/main";
        }

        @RequestMapping("/delete-user/{nr_pomiaru}")
        public String delete(@PathVariable(name = "nr_pomiaru")int nr_pomiaru) {
            dao.delete(nr_pomiaru);
            return "redirect:/main";
        }

    }

    @RequestMapping(value={"/main_admin"})
    public String showAdminPage(Model model) {
        return "admin/main_admin";
    }

    @RequestMapping(value={"/main_user"})
    public String showUserPage(Model model) {
        return "user/main_user";
    }


    @Scheduled(fixedRate = 4000)
    public void updateDatabaseFromCSV() {

        File directory = new File("C:\\PW\\Proj_gr\\Python_dane\\dane");

        if (directory.exists() && directory.isDirectory()) {

            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;

                        while ((line = br.readLine()) != null) {


                            String[] parts = line.split(",");
                            if (parts.length == 11) {
                                Pomiar pomiar = new Pomiar(1, Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), parts[9], parts[10], LocalDateTime.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7])), Double.parseDouble(parts[8]), false);
                                dao.save(pomiar);
                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                file.delete();
            }
        } else {
            System.err.println("Specified directory does not exist or is not a directory.");
        }

    }
}
