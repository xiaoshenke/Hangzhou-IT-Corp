package wuxian.me.lagouspider.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.Main;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.mapper.CompanyMapper;

/**
 * Created by wuxian on 8/4/2017.
 */
@Component
public class ModuleProvider {

    private static ModuleProvider instance;

    public static ModuleProvider getInstance() {
        if (instance == null) {
            instance = Main.ctx.getBean(ModuleProvider.class);
        }
        return instance;
    }

    private ModuleProvider() {
    }

    @Autowired
    public AreaMapper areaMapper;

    @Autowired
    public CompanyMapper companyMapper;
}
