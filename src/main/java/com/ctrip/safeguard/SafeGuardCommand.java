package com.ctrip.safeguard;

import com.ctrip.safeguard.intf.ISemaphore;
import com.ctrip.safeguard.intf.Status;



public abstract class SafeGuardCommand<T> extends BaseCommand<T> {

	private ISemaphore executeSemaphore;
	
	public SafeGuardCommand(CommandSetting setting){
		super(setting.getKey(), setting.getGroup(), Status.Nostart,setting.getSafeGuardConfig());
		executeSemaphore = getOrAddExecuteSemaphores(setting.getKey(), new IsolationSemaphore(setting.getSemaphoreCount()));
		
	
	}
	
	public T execute() throws Exception{
		if(status() == Status.Nostart){
			long startTime = System.currentTimeMillis();
			try{
				return executeForSafeGuard(startTime);
			}catch(Exception e){
				return executeFallback(e);
			}finally{
				metrics.markTotalExecuteLatency(System.currentTimeMillis()-startTime);
			}
		}
		return null;
	}
	
	private T executeForSafeGuard(long startTime){
		if(!safeGuard.allowRequest()){
			throw new RuntimeException("safeguard is open!");
		}
		
		if(executeSemaphore.tryAcquire()){
			try{
				setStatus(Status.Started);
				T result = run();
				long runTime = System.currentTimeMillis() - startTime;
				if(runTime > config.commandTimeout()){
					setStatus(Status.Timeout);
					metrics.markExecuteEvent(Status.Timeout);
				}else{
					setStatus(Status.Success);
					metrics.markExecuteEvent(Status.Success);
					safeGuard.markSuccess();
				}
				return result;
			}catch(Exception e){
				setStatus(Status.Fail);
				metrics.markExecuteEvent(Status.Fail);
				throw new RuntimeException(e);
			}finally{
				executeSemaphore.release();
				metrics.markExecuteLatency(System.currentTimeMillis()-startTime);
			}
		}else{
			setStatus(Status.Reject);
			metrics.markExecuteEvent(Status.Reject);
			throw new RuntimeException("execution was rejected!");
		}
	}
	
	public abstract T run();
	
	public T executeFallback(Exception e) throws Exception{
		throw e;
	}
}
