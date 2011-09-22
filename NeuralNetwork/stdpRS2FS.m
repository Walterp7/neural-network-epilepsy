
endTime=110;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:500:1000)=1;
% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;

%-------------------------depression-IVRS-->IVFS-------------------------------

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


plot(timeEPSP, resultRS2FS);
title('EPSP, IVRS-->IVFS');
xlabel('time [ms]');
ylim([-0.1,1]);


%-------------------------facilitation-VIRS-->VIFS-------------------------------

sd = zeros(3, totalSteps);

% ti =3.0;
% trec = 300.0;
% tfac = 500;

ti =2.0;
trec =70.0;
tfac = 100;%1000;
UU = 0.09;


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


figure('Name', 'Facilitation');

plot(timeEPSP, resultRS2FS);
title('EPSP, VIRS to VIFS');
xlabel('time [ms]');
ylim([-0.1,3]);


%-------------------------depression-II/IIIRS-->II/IIIFS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 110.0;
tfac = 0.00001;
UU = 0.2;
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


plot(timeEPSP, resultRS2FS);
title('EPSP, II/IIIRS-->II/IIIFS');
xlabel('time [ms]');
ylim([-0.1,1]);
