%Simulation for one neuron only

%PARAMETERS FOR RS
a = 0.02;
b = 0.2;
c = -65; 
d = 8;

S = [0, 0,0,0;0, 0,0,0;0,0,0,0;0,0,0,0];


totaltime = 100;%1000;
dt = 0.01;
range = 0:(totaltime/dt);

time = linspace(0,  totaltime, length(range));

v=-65;   % Initial values of v
u=b.*v; % Initial values of u

firings=[];             % spike timings
V =[];
U = [];
Itotal =[];
for t=range            % simulation of 1000 ms
  %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
  I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  firings=[firings; t+0*fired,fired];
  V = [V,v];
  U = [U, u];
  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;                 
end;
figure;
subplot(2,1,1);
plot(time,V(1,:));
title 'potential (v)';
subplot(2,1,2);
plot(time,U(1,:));
title ' (u)';
figure;
plot(time,Itotal(1,:));
title 'input';


