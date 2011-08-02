function [ x ] = ipsp( dt)
tf = 1;
ts = 20;
dt = dt.*heaviside(dt);
x = (exp(-dt/tf)- exp(-dt/ts)).*heaviside(dt+0.05)*10;


end


