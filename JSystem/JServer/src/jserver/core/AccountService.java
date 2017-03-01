package jserver.core;

public interface AccountService{
	Long getAmount(Integer id);
	void addAmount(Integer id, Long value);
	long getStats(String mode);
	void nullifyStats();
}
