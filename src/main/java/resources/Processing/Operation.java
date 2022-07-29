package resources.Processing;

public class Operation {

    /**
     * The or operation is true if either is true so that is the
     * returned boolean
     */
    protected boolean orOp(boolean var1, boolean var2){
        return (var1 || var2);
    }

    /**
     * The and operation is true if both are true so that is the
     * returned boolean
     */
    protected boolean andOp(boolean var1, boolean var2){
        return (var1 && var2);
    }

    /**
     * The conditional or operation is true if both variables have different
     * values so that is the returned boolean
     */
    protected boolean orCOp(boolean var1, boolean var2){
        return var1 != var2;
    }

    /**
     * The if operation is true whenever var1 is false or var2
     * is true, so that is what is returned
     */
    protected boolean ifOp(boolean var1, boolean var2){
        return (!var1 || var2);
    }

    /**
     * The if and only if operation is only true if the
     * two booleans equal each other, which is what is
     * returned
     */
    protected boolean ifOIOp(boolean var1, boolean var2){
        return (var1 == var2);
    }
}
