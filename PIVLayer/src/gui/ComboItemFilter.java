package gui;

public class ComboItemFilter {
	private String filterClassName;
	private String filterName;

	public ComboItemFilter(String filterClassName, String filterName) {
		this.setFilterClassName(filterClassName);
		this.setFilterName(filterName);
	}

	public String getFilterClassName() {
		return filterClassName;
	}

	public void setFilterClassName(String filterClassName) {
		this.filterClassName = filterClassName;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	@Override
	public String toString() {
		return filterName;
	}
}
