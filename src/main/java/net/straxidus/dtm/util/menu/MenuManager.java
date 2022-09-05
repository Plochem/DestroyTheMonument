package net.straxidus.dtm.util.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {
    private Map<UUID, Menu> menus = new HashMap<>();

    public void registerCustomMenu(Menu menu) {
        menus.put(menu.getId(), menu);
    }

    public void deleteCustomMenu(UUID id) {
        menus.remove(id);
    }

    public Map<UUID, Menu> getCustomMenus() {
        return menus;
    }
}
