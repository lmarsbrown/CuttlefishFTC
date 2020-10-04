package com.roboctopi.cuttlefish.utils

class Matrix(var width:Int,var height:Int,init:(Int)->Double = {i-> 0.0}){
    var mat = Array<Double>(width*height,init);

    fun mul(input:Matrix):Matrix
    {
        if(this.width != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        var buffer = Matrix(input.width,this.height);
        for(y in 0 until buffer.height)
        {
            for(x in 0 until buffer.width)
            {
                var item:Double = 0.0;
                for(i in 0 until input.height)
                {
                    item += this.getItem(i,y)*input.getItem(x,i);
                }
                buffer.setItem(x,y,item);
            }
        }
        return buffer;
    }
    fun add(input:Matrix)
    {
        if(this.width != input.width || this.height != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x,y,this.getItem(x,y)+input.getItem(x,y));
            }
        }
    }
    fun subtract(input:Matrix)
    {
        if(this.width != input.width || this.height != input.height)
        {
            throw IllegalArgumentException("Matrix Size Incorrect");
        }
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x,y,this.getItem(x,y)+input.getItem(x,y));
            }
        }
    }

    fun getItem(x:Int,y:Int):Double
    {
        return mat.get(x+y*this.width);
    }
    fun setItem(x:Int,y:Int,value:Double)
    {
        mat.set(x+y*this.width,value);
    }
    fun set(value:Array<Double>)
    {
        this.mat = value;
    }
    fun log()
    {
        var out = "";
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                out += this.getItem(x,y).toString()+" ";
            }
            out += "\n";
        }
        println(out);
    }
    fun runForEach(f:(Int,Int,Double)->Double)
    {
        for(y in 0 until this.height)
        {
            for(x in 0 until this.width)
            {
                this.setItem(x,y,f(x,y,this.getItem(x,y)));
            }
        }
    }
    fun clone():Matrix
    {
        return Matrix(this.width,this.height,{i -> this.mat.get(i)});
    }
    fun scale(scalar:Double)
    {
        this.runForEach({x,y,v-> v * scalar});
    }

}