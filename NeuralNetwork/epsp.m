function [ x ] = epsp( dt)
td = 1;
tr = 5;
dt = dt.*heaviside(dt);
x = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt+0.05);


end

