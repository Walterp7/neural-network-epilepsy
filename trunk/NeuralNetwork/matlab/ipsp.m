function [ x ] = ipsp( dt)
tf = 1;
ts = 15;
dt = dt.*heaviside(dt);
x = (exp(-dt/tf)- exp(-dt/ts)).*heaviside(dt+0.05)/0.76918;


end


