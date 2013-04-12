
endTime=2500;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = ones(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:1000:10020)=1;
% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;
%----------------------facilitation-LTS2RS---------------------------------

sf = zeros(3, totalSteps);

ti =2.0;
trec =70.0;
tfac = 60;%1000;
UU = 0.09;


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



resultLTS2RS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultLTS2RS = resultLTS2RS + sf(3,i)*ipsp(timeEPSP - i*0.1);
        
    end;
end;
normParam = min(resultLTS2RS(1,1:50))
resultLTS2RS = resultLTS2RS/abs(normParam);

% figure('Name','Facilitation');
% x = 0:timestep:(endTime-timestep);
% subplot(4,2,4);
% plot(x,sf(1,:),'.');
% title('u');
% hold on;
% % subplot(2,2,2);
% % plot(x,sf(2,:),'.');
% % title('x');
% subplot(4,2,3);
% plot(timeEPSP, resultRS2LTS);
% title('EPSP');
% hold on;
% subplot(4,2,2);
% plot(x,sf(3,:),'.');
% title('y');
% xlabel('[ms]');
% hold on;
% subplot(4,2,1);
% plot(x,delta);
% title('delta');
% ylim([0 1.5]);
% xlabel('[ms]');
% hold on;
% subplot(4,2,6:8);
% plot(timeEPSP, resultLTS2RS);
% title('EPSP, RS to LTS, 40Hz');
% xlabel('time [ms]');



%----------------------facilitation-RS2LTS---------------------------------

sf = zeros(3, totalSteps);

ti =3.0;
trec = 20%20.0;
tfac = 300%900;%1000;
UU = 0.01%0.02;


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


resultRS2LTS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultRS2LTS = resultRS2LTS + sf(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;
normParam = max(resultRS2LTS(1,1:50))
resultRS2LTS = resultRS2LTS/normParam;


figure('Name','Facilitation');
subplot(4,2,5:8);
plot(timeEPSP, resultLTS2RS);
title('IPSP,  LTS to RS');
xlabel('time [ms]');
%ylim([-2.3, 0.1]);
hold on;
subplot(4,2,1:4);
plot(timeEPSP, resultRS2LTS);
title('EPSP, RS to LTS');
xlabel('time [ms]');
%ylim([-0.1,9]);
%-------------------------depression-RS2FS-------------------------------


sd = zeros(3, totalSteps);

ti =3.0;
trec = 250.0;
tfac = 0.00001;
UU = 0.26;
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

resultRS2FS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultRS2FS = resultRS2FS + sd(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = max(resultRS2FS(1,1:100))
resultRS2FS = resultRS2FS/normParam;

resultFS2RS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultFS2RS = resultFS2RS + sd(3,i)*ipsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = min(resultFS2RS(1,1:100))
resultFS2RS = resultFS2RS/abs(normParam);


figure('Name','Depression');

subplot(4,2,1:4);
plot(timeEPSP, resultRS2FS);
title('EPSP, RS to FS');
xlabel('time [ms]');
ylim([-0.1,1]);
hold on;
subplot(4,2,5:8);
plot(timeEPSP, resultFS2RS);
title('IPSP, FS to RS');
xlabel('time [ms]');
ylim([-1, 0.1]);

%-------------------------depression-RS2RS-------------------------------


sd = zeros(3, totalSteps);

ti =3.0;
trec = 150.0;
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

resultRS2RS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultRS2RS = resultRS2RS + sd(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = max(resultRS2RS(1,1:100))

aaaaa='lala'
resultRS2RS = resultRS2RS/normParam;


figure('Name','Depression');


plot(timeEPSP, resultRS2RS);
title('EPSP, RS to RS');
xlabel('time [ms]');
ylim([-0.1,1]);
hold on;

%-------------------------depression-LTS2FS-------------------------------


sd = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
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

resultLTS2FS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultLTS2FS = resultLTS2FS + sd(3,i)*ipsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = min(resultLTS2FS(1,1:100))

aa='tutaj'
resultLTS2FS = - resultLTS2FS/normParam;


figure('Name','Depression');


plot(timeEPSP, resultLTS2FS);
title('IPSP, LTS to FS');
xlabel('time [ms]');
ylim([-1, 0.1]);
hold on;


%----------------------facilitation-LTS2LTS---------------------------------

sf = zeros(3, totalSteps);

ti =3.0;
trec = 500;
tfac = 1000;
UU = 0.09;


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


resultLTS2LTS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultLTS2LTS = resultLTS2LTS + sf(3,i)*ipsp(timeEPSP - i*0.1);
        
    end;
end;
normParam = min(resultLTS2LTS(1,1:50))
resultLTS2LTS = - resultLTS2LTS/normParam;


figure('Name','Facilitation');
subplot(4,2,5:8);
plot(timeEPSP, resultLTS2LTS);
title('IPSP,  LTS to LTS');
xlabel('time [ms]');
%ylim([-2.3, 0.1]);

%-------------------------depression-FS2LTS-------------------------------


sd = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
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

resultLTS2FS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultLTS2FS = resultLTS2FS + sd(3,i)*ipsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = min(resultLTS2FS(1,1:100))

aa='tutaj'
resultLTS2FS = - resultLTS2FS/normParam;


figure('Name','Depression');


plot(timeEPSP, resultLTS2FS);
title('IPSP, LTS to FS');
xlabel('time [ms]');
ylim([-1, 0.1]);
hold on;
%-------------------------NOTHING--OTHERS ---------------------------------


sd = zeros(3, totalSteps);

ti =3.0;
trec = 1.0;
tfac = 0.0000001;
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

resultRS2FS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultRS2FS = resultRS2FS + sd(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = max(resultRS2FS(1,1:50))
resultRS2FS = resultRS2FS/normParam;

resultFS2RS = zeros(1, totalSteps);
for i = 1:totalSteps
    if (delta(1,i)>0)
        resultFS2RS = resultFS2RS + sd(3,i)*epsp(timeEPSP - i*0.1);
        
    end;
end;

normParam = max(resultFS2RS(1,1:100))
resultFS2RS = resultFS2RS/abs(normParam);


figure('Name','Depression');

subplot(4,2,1:4);
plot(timeEPSP, resultRS2FS);
title('EPSP, NOTHING');
xlabel('time [ms]');
ylim([-0.1,2]);
hold on;
subplot(4,2,5:8);
plot(timeEPSP, resultFS2RS);
title('IPSP, FS to RS');
xlabel('time [ms]');
ylim([-1, 0.1]);
