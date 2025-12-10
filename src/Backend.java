import java.util.Random;

public class Backend {
    private long num;
    private long repeats;

    public Backend(long num,long repeats){
        this.num=num;
        this.repeats=repeats;
    }

    private int getS(){
        int s = 0;
        long num = this.num - 1;

        while(true){
            if(num%2==0){
                s+=1;
                num=num/2;
            }else{
                break;
            }
        }
        return s;
    }

    private long getD(int s){
        return ((this.num-1)/(long)Math.pow(2,s));
    }

    private long getRandomNum(){
        long r = new Random().nextLong(this.num);
        if(r==0) r=1;
        return r;
    }

    public boolean run(){
        Log logger = new Log();
        int s = getS();
        long d = getD(s);
        logger.writeLog("\n"+(this.num-1) + " = 2^"+s+" * "+d+"\n",true);
        long k=0;
        for(int i=0;i<repeats;i++) {
            boolean isPrime=false;
            long randNum = getRandomNum();
            logger.writeLog("random number = " + randNum + ";",true);
            for (int n = 0; n < s; n++){
                long pow=(long) Math.pow(2,n) * d;
                logger.writeLog(randNum+"^"+pow+" mod "+ this.num,true);
                FastPow fastPow = new FastPow(randNum, pow, this.num);
                long remaining = fastPow.run();

                if(remaining==this.num-1 || remaining==1){
                    isPrime=true;
                    k+=1;
                    break;
                }
            }
            if(isPrime) {
                logger.writeLog("Given number " + this.num + " is prime. Witness: " + randNum+"\n",true);
            }else{
                logger.writeLog("Given number " + this.num + " is not prime.\n",true);
                break;
            }

            try {
                Thread.sleep(100); //for better random
            }catch (Exception e){
                System.out.println("Error! "+e);
            }
        }
        if(k==repeats){
            logger.writeLog("\nResult: "+k+" out of "+this.repeats+" iterations proved primarity of "+this.num,true);
            return true;
        }else{
            return false;
        }
    }
}
