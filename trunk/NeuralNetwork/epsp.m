function [ x ] = epsp( dt)
td = 0.05;
tr = 24;
%dt = dt.*heaviside(dt);
x = (exp(-dt/tr)- exp(-dt/td)).*heaviside(dt+0.05);


end

