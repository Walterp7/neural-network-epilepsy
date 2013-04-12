%Simulation for one neuron only
set(0,'DefaultAxesFontName', 'Arial');
set(0,'DefaultAxesFontSize', 12);
set(gca,'Box','off')
% Change default text fonts.
set(0,'DefaultTextFontname', 'Arial');
set(0,'DefaultTextFontSize', 12);

%PARAMETERS FOR RS
a = 0.02;
b = 0.2;
c = -65; 
d = 8;

S = [0, 0,0,0;0, 0,0,0;0,0,0,0;0,0,0,0];


totaltime = 200;%1000;
amplitude = 10;
color = 'k';
dt = 0.1;
range = 0:(totaltime/dt);

time = linspace(0,  totaltime, length(range));

v=-65;   % Initial values of v
u=b.*v; % Initial values of u

I = ones(1,totaltime/dt+1)*amplitude;
I(1:100)=0;

sd = zeros(3, totaltime/dt+1);
timeEPSP = 0.1:dt:totaltime;
ti =3.0;
trec = 10;
tfac = 10;
UU = 0.15;

sd(:,1)=[0;1; 0];

firings=[];             % spike timings
V =[];
U = [];
%Itotal =[];
lastSpike= 1;
resultRS2RS = zeros(1, totaltime/dt);
for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    V = [V,v];
  U = [U, u];
  if(fired == 1)
      deltat = abs(lastSpike - t)*timestep;
      if(deltat<6.25)
          v(fired)=30;
          fired = [];
      end;
          
  end;

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RS = resultRS2RS + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;
%Itotal = I;

resultRS2RSold = zeros(1, totaltime/dt);
Vold =[];
Uold = [];
firings=[];   
v=-65;   % Initial values of v
u=b.*v; % Initial values of u

for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    Vold = [Vold,v];
  Uold = [Uold, u];
  

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RSold  = resultRS2RSold  + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;

figure;

subplot(6,2,3);
plot(time,V(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
%title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,4);
plot(timeEPSP,resultRS2RS, color, 'LineWidth', 1,  'LineSmoothing','on');
%title('PSP');
xlabel('time[ms]')
ylabel('v [ms]')
%%%%
subplot(6,2,1);
plot(time,Vold(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,2);
plot(timeEPSP,resultRS2RSold, color, 'LineWidth', 1,  'LineSmoothing','on');
title('PSP');
xlabel('time[ms]')
ylabel('v [ms]')

%plot(time,Itotal(1,:));
% plot(time,I);
% title 'input';
% ylim([0, amplitude+5]);
% figure;
% plot(timeEPSP,resultRS2RS);
% title 'PSP';

%ylim([0, amplitude+5]);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%DIFFERENT AMPLITUDE

a = 0.02;
b = 0.2;
c = -65; 
d = 8;

S = [0, 0,0,0;0, 0,0,0;0,0,0,0;0,0,0,0];



amplitude = 50;
color = 'b';
dt = 0.1;
range = 0:(totaltime/dt);

time = linspace(0,  totaltime, length(range));

v=-65;   % Initial values of v
u=b.*v; % Initial values of u

I = ones(1,totaltime/dt+1)*amplitude;
I(1:100)=0;

sd = zeros(3, totaltime/dt+1);
timeEPSP = 0.1:dt:totaltime;
ti =3.0;
trec = 10;
tfac = 0.000001;
UU = 0.15;

sd(:,1)=[0;1; 0];

firings=[];             % spike timings
V =[];
U = [];
%Itotal =[];
lastSpike= 1;
resultRS2RS = zeros(1, totaltime/dt);
for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    V = [V,v];
  U = [U, u];
  if(fired == 1)
      deltat = abs(lastSpike - t)*timestep;
      if(deltat<6.25)
          v(fired)=30;
          fired = [];
      end;
          
  end;

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RS = resultRS2RS + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;
%Itotal = I;

resultRS2RSold = zeros(1, totaltime/dt);
Vold =[];
Uold = [];
firings=[];   
v=-65;   % Initial values of v
u=b.*v; % Initial values of u

for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    Vold = [Vold,v];
  Uold = [Uold, u];
  

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RSold  = resultRS2RSold  + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;



subplot(6,2,7);
plot(time,V(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
%title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,8);
plot(timeEPSP,resultRS2RS, color, 'LineWidth', 1,  'LineSmoothing','on');
%title('PSP');
xlabel('time[ms]')
ylabel('v [ms]')
%%%%
subplot(6,2,5);
plot(time,Vold(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
%title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,6);
plot(timeEPSP,resultRS2RSold, color, 'LineWidth', 1,  'LineSmoothing','on');
%title('PSP');
xlabel('time[ms]')
ylabel('v [ms]')

%plot(time,Itotal(1,:));
% plot(time,I);
% title 'input';
% ylim([0, amplitude+5]);
% figure;
% plot(timeEPSP,resultRS2RS);
% title 'PSP';
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%DIFFERENT AMPLITUDE

a = 0.02;
b = 0.2;
c = -65; 
d = 8;

S = [0, 0,0,0;0, 0,0,0;0,0,0,0;0,0,0,0];



amplitude = 100;
color = 'r';
dt = 0.1;
range = 0:(totaltime/dt);

time = linspace(0,  totaltime, length(range));

v=-65;   % Initial values of v
u=b.*v; % Initial values of u

I = ones(1,totaltime/dt+1)*amplitude;
I(1:100)=0;

sd = zeros(3, totaltime/dt+1);
timeEPSP = 0.1:dt:totaltime;
ti =3.0;
trec = 10;
tfac = 0.000001;
UU = 0.15;

sd(:,1)=[0;1; 0];

firings=[];             % spike timings
V =[];
U = [];
%Itotal =[];
lastSpike= 1;
resultRS2RS = zeros(1, totaltime/dt);
for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    V = [V,v];
  U = [U, u];
  if(fired == 1)
      deltat = abs(lastSpike - t)*timestep;
      if(deltat<6.25)
          v(fired)=30;
          fired = [];
      end;
          
  end;

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RS = resultRS2RS + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;
%Itotal = I;

resultRS2RSold = zeros(1, totaltime/dt);
Vold =[];
Uold = [];
firings=[];   
v=-65;   % Initial values of v
u=b.*v; % Initial values of u

for t=range            % simulation of 1000 ms
 %I=  random('Normal',0,15,1,1);%10*[0;0;0;0]*heaviside(t-400);%*heaviside(250-time(t)); 
 %I = 10*(exp(-t*dt/20)-exp(-t*dt/0.1));
  %I = 100;
 % Itotal=[Itotal,I];
  fired=find(v>=30);    % indices of spikes
  %firings=[firings; t+0*fired,fired];
    Vold = [Vold,v];
  Uold = [Uold, u];
  

  v(fired)=c(fired);
  u(fired)=u(fired)+d(fired);
 % I=I+sum(S(:,fired),2);
  v=v+dt*(0.04*v.^2+5*v+140-u+I(1,t+1)); 
 % v=v+0.5*dt*(0.04*v.^2+5*v+140-u+I); 
  u=u+a.*(b.*v-u)*dt;     
    if(fired == 1)
        deltat = abs(lastSpike - t)*timestep;
   
        P = Pmatr( deltat, tfac, trec, ti);
        sd(:,t) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,t)); -sd(2,t)*(sd(1,t)+UU*(1-sd(1,t)));sd(2,t)*(sd(1,t)+UU*(1-sd(1,t))) ];
        sd(:,t) = sd(:,t)+s0;
        lastSpike = t;             
     
        resultRS2RSold  = resultRS2RSold  + (sd(3,t)/UU)*epsp(timeEPSP - t*timestep)/0.8870;
         
    end
end;



subplot(6,2,11);
plot(time,V(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
%title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,12);
plot(timeEPSP,resultRS2RS, color, 'LineWidth', 1,  'LineSmoothing','on');
%title('PSP');
xlabel('time[ms]')
ylabel('v [ms]')
%%%%
subplot(6,2,9);
plot(time,Vold(1,:), color, 'LineWidth', 1,  'LineSmoothing','on')
%title 'Membrane Potential';
xlabel('time[ms]')
ylabel('v [ms]')
subplot(6,2,10);
plot(timeEPSP,resultRS2RSold, color, 'LineWidth', 1,  'LineSmoothing','on');
%title('PSP');
ylim( [0 15])
xlabel('time[ms]')
ylabel('v [ms]')


