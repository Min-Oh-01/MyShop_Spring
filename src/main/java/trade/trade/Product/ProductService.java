package trade.trade.Product;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import trade.trade.FileService;
import trade.trade.ProductDeleteDTO;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    // 모든 상품 조회 메서드
    List<ProductDTO> productFindAll() {
        return productRepository.findAll().stream().map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productEntity.getId());
            productDTO.setProductImage(productEntity.getProductImage());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setProductOutline(productEntity.getProductOutline());
            productDTO.setProductPrice(productEntity.getProductPrice());
            productDTO.setProductDate(productEntity.getProductDate());
            productDTO.setStartDate(productEntity.getStartDate());
            productDTO.setEndDate(productEntity.getEndDate());
            return productDTO;
        }).collect(Collectors.toList());
    }

    // 상품 등록 메서드
    void productSave(ProductCreateDTO productCreateDTO, String imagePath) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductImage(imagePath);
        productEntity.setProductName(productCreateDTO.getName());
        productEntity.setProductPrice(productCreateDTO.getPrice());
        productEntity.setProductOutline(productCreateDTO.getProductOutline());
        productEntity.setProductDate(productCreateDTO.getProductDate());
        productEntity.setStartDate(productCreateDTO.getStartDate());
        productEntity.setEndDate(productCreateDTO.getEndDate());

        productRepository.save(productEntity);
    }

    // 특정 ID 상품 조회 (상품 상세 페이지)
    Optional<ProductDTO> productFindById(Long id) {
        return productRepository.findById(id).map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(productEntity.getId());
            productDTO.setProductImage(productEntity.getProductImage());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setProductOutline(productEntity.getProductOutline());
            productDTO.setProductPrice(productEntity.getProductPrice());
            productDTO.setProductDate(productEntity.getProductDate());
            productDTO.setStartDate(productEntity.getStartDate());
            productDTO.setEndDate(productEntity.getEndDate());

            System.out.println("Product Image: " + productEntity.getProductImage());
            return productDTO;
        });
    }

    // 상품 수정 메서드
    void productUpdate(ProductUpdateDTO productUpdateDTO) {
        ProductEntity productEntity = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(()-> new NoSuchElementException("상품이 존재하지 않습니다."));

        String imagePath = productUpdateDTO.getOriginalImage();

        System.out.println("Original Image Path: " + imagePath);
        if (productUpdateDTO.getImage() != null && !productUpdateDTO.getImage().isEmpty()) {
            try {
                fileService.fileDelete(String.valueOf(Paths.get(productUpdateDTO.getOriginalImage()).getFileName()));
                imagePath = fileService.imageSave(productUpdateDTO.getImage());

                System.out.println("New Image Path: " + imagePath);

            } catch (IOException e) {
                throw new RuntimeException("이미지 처리 중 오류 발생", e);
            }
        }

        productEntity.setProductImage(imagePath);
        productEntity.setProductName(productUpdateDTO.getProductName());
        productEntity.setProductPrice(productUpdateDTO.getProductPrice());
        productEntity.setProductOutline(productUpdateDTO.getProductOutline());
        productEntity.setProductDate(productUpdateDTO.getProductDate());
        productEntity.setStartDate(productUpdateDTO.getStartDate());
        productEntity.setEndDate(productUpdateDTO.getEndDate());

        productRepository.save(productEntity);
    }


    // 상품 삭제 메서드
    void productDelete(ProductDeleteDTO productDeleteDTO) {
        ProductEntity productEntity = productRepository.findById(productDeleteDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        String imagePath = productEntity.getProductImage();
        if (imagePath != null && imagePath.startsWith("/upload/images/")) {
            String fileName = Paths.get(imagePath).getFileName().toString();
            try {
                fileService.fileDelete(fileName);
            } catch (IOException e) {
                throw new RuntimeException("이미지 삭제 중 오류 발생", e);
            }
        }
        productRepository.deleteById(productEntity.getId());
    }

    // 상품 검색 메서드
    Page<ProductDTO> productSearch(String keyword, Pageable pageable) {
        Page<ProductEntity> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(productEntity.getId());
            productDTO.setProductImage(productEntity.getProductImage());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setProductPrice(productEntity.getProductPrice());
            productDTO.setProductOutline(productEntity.getProductOutline());
            return productDTO;
        });

    }

//    return products.stream().map(productEntity -> {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setId(productEntity.getId());
//        productDTO.setProductImage(productEntity.getProductImage());
//        productDTO.setProductName(productEntity.getProductName());
//        productDTO.setProductPrice(productEntity.getProductPrice());
//        productDTO.setProductOutline(productEntity.getProductOutline());
//        return productDTO;
//    }).collect(Collectors.toList());

    // 상품 목록 페이지네이션, 페이지 수
    Page<ProductDTO> productListPagination(String keyword, Pageable pageable) {
        Page<ProductEntity> productPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage.map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(productEntity.getId());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setProductImage(productEntity.getProductImage());
            productDTO.setProductPrice(productEntity.getProductPrice());
            productDTO.setProductOutline(productEntity.getProductOutline());
            productDTO.setProductDate(productEntity.getProductDate());
            productDTO.setStartDate(productEntity.getStartDate());
            productDTO.setEndDate(productEntity.getEndDate());
            return productDTO;
        });
    }


    // 가격 조회해서 내보내기
    Page<ProductDTO> getProductByPriceRange(String priceRange, Pageable pageable) {
        Page<ProductEntity> products;

        if("under-10000".equals(priceRange)) {
            products = productRepository.findByProductPriceLessThan(10000F, pageable);
        } else if ("10000-50000".equals(priceRange)) {
            products = productRepository.findByProductPriceBetween(10000F, 50000F, pageable);
        } else if ("above-50000".equals(priceRange)) {
            products = productRepository.findByProductPriceGreaterThan(50000F, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(productEntity.getId());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setProductImage(productEntity.getProductImage());
            productDTO.setProductPrice(productEntity.getProductPrice());
            productDTO.setProductOutline(productEntity.getProductOutline());
            productDTO.setProductDate(productEntity.getProductDate());
            productDTO.setStartDate(productEntity.getStartDate());
            productDTO.setEndDate(productEntity.getEndDate());
            return productDTO;
        });
    }
}



//productEntity.getId(),
//                productEntity.getProductImage(),
//                productEntity.getProductName(),
//                productEntity.getProductOutline(),
//                productEntity.getProductPrice(),
//                productEntity.getProductDate(),
//                productEntity.getStartDate(),
//                productEntity.getEndDate()