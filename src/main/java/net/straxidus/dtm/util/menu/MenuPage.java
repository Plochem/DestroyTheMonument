package net.straxidus.dtm.util.menu;

import net.straxidus.dtm.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuPage extends Menu {

    private MenuButton prevPage;
    private MenuButton nextPage;
    private PaginatedMenu parentMenu;

    public MenuPage next;
    public MenuPage prev;

    public MenuPage(PaginatedMenu parentMenu, String title, int size, boolean deleteOnClose) {
        super(title, size, deleteOnClose);
        this.parentMenu = parentMenu;
    }
    public void addNextPageButton() {
        nextPage = new MenuButton(new ItemBuilder(Material.ARROW).displayname("next page").build()) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                next.show((Player) e.getWhoClicked());
            }
        };
        this.addButton(nextPage, parentMenu.getNextButtonSlot());
    }

    public void addPreviousPageButton() {
        prevPage = new MenuButton(new ItemBuilder(Material.ARROW).build()) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                prev.show((Player) e.getWhoClicked());
            }
        };
        this.addButton(prevPage, parentMenu.getPrevButtonSlot());
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return prev != null;
    }

    public PaginatedMenu getParentMenu() {
        return parentMenu;
    }
}
