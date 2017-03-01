package jserver;

import java.util.concurrent.atomic.AtomicLong;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jserver.core.AccountService;
import jserver.storage.Storage;

@Singleton
public class SmartAccountService implements AccountService{

	private Storage store;
	private AtomicLong writes;
	private AtomicLong reads;
	private long anchorTime;
	@Inject
	public SmartAccountService(Storage store){
		this.store = store;
		writes = new AtomicLong();
		reads = new AtomicLong();
		anchorTime = System.currentTimeMillis();
	}
	
	public Long getAmount(Integer id) {
		reads.getAndIncrement();
		return store.getAmount(id);
	}

	public void addAmount(Integer id, Long value) {
		writes.getAndIncrement();
		store.addAmount(id, value);
	}

	@Override
	public long getStats(String target) {
		if (target.equalsIgnoreCase("writes")){
			return writes.get();
		}else if(target.equalsIgnoreCase("reads")){
			return reads.get();
		}else if(target.equalsIgnoreCase("readsperms")){
			return reads.get() / (System.currentTimeMillis() - anchorTime);
		}else if(target.equalsIgnoreCase("writesperms")){
			return writes.get() / (System.currentTimeMillis() - anchorTime);
		}else{
			return Long.MIN_VALUE;
		}
	}
	
	public void nullifyStats(){
		writes.set(0);
		reads.set(0);
		anchorTime = System.currentTimeMillis();
	}

}
