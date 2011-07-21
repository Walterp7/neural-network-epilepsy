
endTime=2500;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
delta(3:1000:8002)=1;
delta(16000:1000:23000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;
%----------------------facilitation---------------------------------

sf = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
tfac = 500;%1000;
UU = 0.04;


sf(:,1)=[0;1; 0];
lastSpike = 1;
for i=2:totalSteps
    if(delta(i) == 1)
        dt = abs(lastSpike - i)*timestep;
   
        P = Pmatr( dt, tfac, trec, ti);
        sf(:,i) = P* [sf(:,lastSpike);1];
        s0=[UU*(1- sf(1,i)); -sf(2,i)*(sf(1,i)+UU*(1-sf(1,i)));sf(2,i)*(sf(1,i)+UU*(1-sf(1,i))) ];
        sf(:,i) = sf(:,i)+s0;
        lastSpike = i;
    end;
   
end

% for i=2:totalSteps
%     if(delta(i) == 1)
%         dt = abs(lastSpike - i)*timestep;
%    
%         P = Pmatr( dt, tfac, trec, ti);
%         sf(:,i) = P* [sf(:,lastSpike);1];
%         s0=[UU*(1- sf(1,i)); -sf(2,i)*(sf(1,i)+UU*(1-sf(1,i)));sf(2,i)*(sf(1,i)+UU*(1-sf(1,i))) ];
%         sf(:,i) = sf(:,i)+s0;
%         lastSpike = i;
%     else
%         dt = timestep;
%         sf(:,i) = P* [sf(:,i-1);1];
%     end;
%    
% end

resultF = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultF = resultF + sf(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;
figure;
plot(timeEPSP, resultF);
title('EPSP, RS to LTS, 10Hz');
xlabel('time [ms]');


figure('Name','Facilitation');
x = 0:timestep:(endTime-timestep);
subplot(2,2,4);
plot(x,sf(1,:),'.');
title('u');
hold on;
% subplot(2,2,2);
% plot(x,sf(2,:),'.');
% title('x');
subplot(2,2,3);
plot(timeEPSP, resultF);
title('EPSP');
hold on;
subplot(2,2,2);
plot(x,sf(3,:),'.');
title('y');
xlabel('[ms]');
hold on;
subplot(2,2,1);
plot(x,delta);
title('delta');
ylim([0 1.5]);
xlabel('[ms]');
hold on;

%-------------------------depression--------------------------------



sd = zeros(3, totalSteps);

ti =3.0;
trec = 800.0;
tfac = 0.00001;
UU = 0.5;
sd(:,1)=[0;1; 0];
lastSpike = 1;
for i=2:totalSteps
    if(delta(i) == 1)
        dt = abs(lastSpike - i)*timestep;
   
        P = Pmatr( dt, tfac, trec, ti);
        sd(:,i) = P* [sd(:,lastSpike);1];
        s0=[UU*(1- sd(1,i)); -sd(2,i)*(sd(1,i)+UU*(1-sd(1,i)));sd(2,i)*(sd(1,i)+UU*(1-sd(1,i))) ];
        sd(:,i) = sd(:,i)+s0;
        lastSpike = i;
    end;
   
end

resultF = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultF = resultF + sf(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;
figure;
plot(timeEPSP, resultF);
title('EPSP, RS to LTS, 10Hz');
xlabel('time [ms]');

figure('Name','Depression');
x = 0:timestep:(endTime-timestep);
subplot(2,2,1);
plot(x,sd(1,:),'.');
xlim([0 2500]);
title('u');
hold on;

subplot(2,2,2);
plot(x,sd(2,:),'.');
title('x');

hold on;
subplot(2,2,3);
plot(x,sd(3,:),'.');
title('y');

xlabel('[ms]');
hold on;

subplot(2,2,4);
plot(x,delta);
title('input');
ylim([0 1.5]);

xlabel('[ms]');
hold on;
