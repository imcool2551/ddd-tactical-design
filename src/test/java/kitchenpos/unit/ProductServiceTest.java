package kitchenpos.unit;

import kitchenpos.common.Profanity;
import kitchenpos.menus.domain.Menu;
import kitchenpos.menus.domain.MenuProduct;
import kitchenpos.menus.domain.MenuRepository;
import kitchenpos.products.application.ProductService;
import kitchenpos.products.domain.Product;
import kitchenpos.products.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    Profanity profanity;

    @InjectMocks
    ProductService productService;


    @DisplayName("상품의 가격 0원보다 작을 수 없다.")
    @Test
    void create_IllegalPrice() {
        // given
        Product request = aChickenProduct(-10_000);

        // when + then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "상품의 이름은 빈 값을 허용하지 않는다. source = {0}")
    @NullSource
    void create_EmptyName(String source) {
        // given
        Product request = aProduct(source, 10_000);

        // when + then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "상품의 이름은 비속어를 허용하지 않는다. source = {0}")
    @ValueSource(strings = {"바보"})
    void create_ProfaneName(String source) {
        // given
        Product request = aProduct(source, 10_000);

        when(profanity.contains("바보")).thenReturn(true);

        // when + then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        Product request = aChickenProduct(10_000);

        when(productRepository.save(any())).then(i -> i.getArgument(0, Product.class));
        when(profanity.contains(any())).thenReturn(false);

        // when
        Product saved = productService.create(request);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @ParameterizedTest(name = "상품의 가격을 변경하면 상품을 포함하는 메뉴들은 가격에 따라 숨겨질 수 있다. price = {0}")
    @MethodSource("provideArgumentsForChangePrice")
    void changePrice(int originalPrice, boolean expected) {
        // given
        Product product = aChickenProduct(10_000);
        MenuProduct menuProduct = aMenuProduct(product, 1);
        Menu menu = aMenu("후라이드 치킨", originalPrice, menuProduct);

        Product changePriceRequest = new Product();
        changePriceRequest.setPrice(BigDecimal.valueOf(9_500));

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(menuRepository.findAllByProductId(product.getId()))
                .thenReturn(Collections.singletonList(menu));

        // when
        productService.changePrice(product.getId(), changePriceRequest);

        // then
        assertThat(menu.isDisplayed()).isEqualTo(expected);
    }

    public static Stream<Arguments> provideArgumentsForChangePrice() {
        return Stream.of(
                Arguments.of(9_400, true),
                Arguments.of(9_500, true),
                Arguments.of(9_600, false)
        );
    }
}
