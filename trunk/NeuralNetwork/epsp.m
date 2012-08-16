function [ x ] = epsp( dt)

td = 0.5;
tr = 20;
%dt = dt.*heaviside(dt);
x = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt+0.05);
x(isnan(x)) = 0;


end

