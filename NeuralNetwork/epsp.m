function [ x ] = epsp( dt)

td = 0.1;
tr = 15;
%dt = dt.*heaviside(dt);
x = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt+0.05);


end

