package net.straxidus.dtm.util.menu;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaginatedMenu {

    private MenuPage head;
    private int length;
    private String title;
    private int size;
    private boolean deleteOnClose;
    private int prevButtonSlot;
    private int nextButtonSlot;

    public PaginatedMenu(String title, int size, boolean deleteOnClose, int prevButtonSlot, int nextButtonSlot) {
        this.title = title;
        this.size = size;
        this.deleteOnClose = deleteOnClose;
        this.length = 0;
        this.prevButtonSlot = prevButtonSlot;
        this.nextButtonSlot = nextButtonSlot;
    }

    public void createAllPages(int[] layout, List<MenuButton> buttons) {
        int i = 0;
        Map<Integer, MenuButton> page = new HashMap<>();
        for (MenuButton button : buttons) {
            if (i == layout.length) {
                i = 0;
                appendNewPage(page);
                page.clear();
            }
            page.put(layout[i], button);
            i++;
        }
        if (page.size() > 0) {
            appendNewPage(page);
        }
    }

    public void appendNewPage(Map<Integer, MenuButton> layout) {
        length++;
        MenuPage newPage = new MenuPage(this, title + " (Page " + length + ")", size, deleteOnClose);
        for (Map.Entry<Integer, MenuButton> buttonLayout : layout.entrySet()) {
            newPage.addButton(buttonLayout.getValue(), buttonLayout.getKey());
        }
        if (head == null) {
            head = newPage;
        } else {
            MenuPage oldTail = getTail();
            oldTail.next = newPage;
            newPage.prev = oldTail;

            oldTail.addNextPageButton();
            newPage.addPreviousPageButton();
        }
    }

    public MenuPage getHead() {
        return head;
    }

    public MenuPage getTail() {
        MenuPage curr = head;
        while (curr.hasNext()) {
            curr = curr.next;
        }
        return curr;
    }

    public boolean hasViewerOnAnyPage() {
        MenuPage iterator = head;
        if (iterator.getInventory().getViewers().size() != 0) return true;
        while (iterator.hasNext()) {
            iterator = iterator.next;
            if (iterator.getInventory().getViewers().size() != 0) return true;
        }
        return false;
    }

    public void delete() {
        MenuPage iterator = head;
        while (iterator.hasNext()) {
            iterator = iterator.next;
            iterator.delete();
        }
        head.delete();
    }

    public void show(Player p) {
        head.show(p);
    }

    public int getLength() {
        return length;
    }

    public int getPrevButtonSlot() {
        return prevButtonSlot;
    }

    public int getNextButtonSlot() {
        return nextButtonSlot;
    }
}
