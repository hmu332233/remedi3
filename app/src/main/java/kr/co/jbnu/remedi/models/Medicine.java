package kr.co.jbnu.remedi.models;

public class Medicine {

	private String name;			//이름
	private String ingredient; 		//주성분
	private String ingredientCount; //주성분수
	private String enterprise;		//업체
	private String category; 		//전문,일반 의약품	
	private String codeNumber; 		//품목기준코드
	
	private String icon; 			//성상
	private String shape;			//모양
	private String categoryCode;	//분류번호
	private String permissionDate;	//허가일
	private String standardCode;	//표준코드
	
	private String effect;			//효능효과
	private String usage;			//용법용량
	private String attention;		//주의사항
	
	
	
	public void addInfo( String icon, String shape, String categoryCode, String permissionDate,
			String standardCode, String effect, String usage, String attention) {
		this.icon = icon;
		this.shape = shape;
		this.categoryCode = categoryCode;
		this.permissionDate = permissionDate;
		this.standardCode = standardCode;
		this.effect = effect;
		this.usage = usage;
		this.attention = attention;
	}

	public Medicine()
	{
		System.out.println("생성");
	}
	
	public Medicine( 
			String _name, String _ingredient, String _ingredientCount, 
			String _enterprise, String _category, String  _codeNumber)
	{
		name = _name;
		ingredient = _ingredient;
		ingredientCount = _ingredientCount;
		enterprise = _enterprise;
		category = _category;
		codeNumber = _codeNumber;
	}

	public String getName() {
		return name;
	}

	public String getIngredient() {
		return ingredient;
	}

	public String getIngredientCount() {
		return ingredientCount;
	}

	public String getEnterprise() {
		return enterprise;
	}

	public String getCategory() {
		return category;
	}

	public String getCodeNumber() {
		return codeNumber;
	}

	public String getIcon() {
		return icon;
	}

	public String getShape() {
		return shape;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public String getPermissionDate() {
		return permissionDate;
	}

	public String getStandardCode() {
		return standardCode;
	}

	public String getEffect() {
		return effect;
	}

	public String getUsage() {
		return usage;
	}

	public String getAttention() {
		return attention;
	}
	
	public void print()
	{
		System.out.println(name);
		System.out.println(ingredient);
		System.out.println(ingredientCount);
		System.out.println(enterprise);
		System.out.println(category);
		System.out.println(codeNumber);
		System.out.println(icon);
		System.out.println(shape);
		System.out.println(categoryCode);
		System.out.println(permissionDate);
		System.out.println(standardCode);
		System.out.println(effect);
		System.out.println(usage);
		System.out.println(attention);
	}
	
}
