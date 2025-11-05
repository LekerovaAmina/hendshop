package kz.handshop.dto.response;

public class ShelfResponse {

    private Long id;
    private String shelfName;
    private String description;
    private CategoryResponse globalCategory;

    public ShelfResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryResponse getGlobalCategory() {
        return globalCategory;
    }

    public void setGlobalCategory(CategoryResponse globalCategory) {
        this.globalCategory = globalCategory;
    }
}
