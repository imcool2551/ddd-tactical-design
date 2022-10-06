package kitchenpos;

import kitchenpos.eatinorders.domain.Order;
import kitchenpos.eatinorders.domain.OrderLineItem;
import kitchenpos.eatinorders.domain.OrderTable;
import kitchenpos.eatinorders.domain.OrderType;
import kitchenpos.menus.domain.Menu;
import kitchenpos.menus.domain.MenuGroup;
import kitchenpos.menus.domain.MenuProduct;
import kitchenpos.products.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class Fixtures {

    public static MenuGroup aMenuGroup() {
        return new MenuGroup("한마리치킨");
    }

    public static Product aChickenProduct(int price) {
        return aProduct("후라이드 치킨", price);
    }

    public static Product aProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static MenuProduct aChickenMenuProduct(int price, int quantity) {
        return aMenuProduct(aChickenProduct(price), quantity);
    }

    public static MenuProduct aMenuProduct(Product product, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(product);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu aManWonChickenMenu(int price) {
        return aMenu("후라이드 치킨", price, aChickenMenuProduct(1, 10_000));
    }

    public static Menu aMenu(String name, int price, MenuProduct... menuProducts) {
        return aMenu(name, price, aMenuGroup(), menuProducts);
    }

    public static Menu aMenu(String name, int price, MenuGroup menuGroup, MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(List.of(menuProducts));
        menu.setDisplayed(true);
        menu.setMenuGroup(menuGroup);
        menu.setMenuGroupId(menuGroup.getId());
        return menu;
    }

    public static OrderLineItem anOrderLineItem(Menu menu, int quantity) {
        OrderLineItem oli = new OrderLineItem();
        oli.setMenu(menu);
        oli.setMenuId(menu.getId());
        oli.setQuantity(quantity);
        oli.setPrice(menu.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return oli;
    }

    public static OrderTable anOrderTable(boolean occupied) {
        return anOrderTable("1번 테이블", occupied);
    }

    public static OrderTable anOrderTable(String name, boolean occupied) {
        return anOrderTable(name, occupied, 0);
    }

    public static OrderTable anOrderTable(String name, boolean occupied, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setName(name);
        orderTable.setOccupied(occupied);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order aDeliveryOrder(String deliveryAddress, OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setType(OrderType.DELIVERY);
        order.setOrderLineItems(List.of(orderLineItems));
        order.setDeliveryAddress(deliveryAddress);
        return order;
    }

    public static Order anEatInOrder(OrderTable orderTable, OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setType(OrderType.EAT_IN);
        order.setOrderLineItems(List.of(orderLineItems));
        order.setOrderTable(orderTable);
        order.setOrderTableId(orderTable.getId());
        return order;
    }
}
