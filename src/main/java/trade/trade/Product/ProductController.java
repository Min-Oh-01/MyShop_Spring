package trade.trade.Product;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import trade.trade.FileService;
import trade.trade.ProductDeleteDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Getter
@Setter

public class ProductController {
    private final ProductService productService;
    private final FileService fileService;

    //private final String uploadDir = "C:/upload/"; // 파일 저장 경로

//  홈페이지
    @GetMapping({"/", "/home"})
    String marketHome(Model model) {
        List<ProductDTO> products = productService.productFindAll();
        model.addAttribute("products", products);
        return "home";
    }

    // 상품 목록
    @GetMapping("/productList")
    String productList(@RequestParam(defaultValue = "0") Integer page,
                       String keyword, String priceRange, Model model) {
        Pageable pageable = PageRequest.of(page, 6);

        // Page<ProductDTO> productPage = productService.productListPagination(keyword, pageable);
        Page<ProductDTO> productPage;

        if (priceRange != null ) {
            productPage = productService.getProductByPriceRange(priceRange, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productService.productSearch(keyword, pageable);
        } else {
            productPage = productService.productListPagination("", pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        model.addAttribute("priceRange", priceRange);

        return "product/productList";
    }

    // 상품 등록 페이지 접속
    @GetMapping("/productRegister")
    String productRegister() {
        return "product/productRegistration";
    }

    // 상품 등록 페이지 - 등록
    @PostMapping("/productRegister")
    String productRegister(@ModelAttribute ProductCreateDTO productCreateDTO) throws IOException {
        String imagePath = fileService.imageSave(productCreateDTO.getImage());
        productService.productSave(productCreateDTO, imagePath);
        System.out.println(productCreateDTO.getName());
        return "redirect:/productList";
    }

    // 상품 상세 페이지
    @GetMapping("/productDetail/{id}")
    String productDetail(@PathVariable Long id, Model model) {
        Optional<ProductDTO> product = productService.productFindById(id);

        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/productDetail";
        } else {
            System.out.println("상품 정보 없음");
            return "redirect:/productList";
        }
    }

    // 상품 수정 페이지
    @GetMapping("/productEdit/{id}")
    String productEdit(@PathVariable Long id, Model model) {
        Optional<ProductDTO> product = productService.productFindById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/productEdit";
        } else {
            return "redirect:/productList";
        }
    }

    // 수정한 데이터 저장
    @PostMapping("/productEdit")
    String productEdit(@ModelAttribute ProductUpdateDTO productUpdateDTO) {
        System.out.println("Received originalImage: " + productUpdateDTO.getOriginalImage());
        productService.productUpdate(productUpdateDTO);
        return "redirect:/productDetail/" + productUpdateDTO.getId();
    }

    // 상품 삭제 요청
    @PostMapping("/productDelete")
    String productDelete(@ModelAttribute ProductDeleteDTO productDeleteDTO) {
        productService.productDelete(productDeleteDTO);
        return "redirect:/productList";
    }



}
