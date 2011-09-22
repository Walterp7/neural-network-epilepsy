%endTime=1500;
endTime=200;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:500:1050)=1;
% delta(3)=1;
% delta(253)=1
% delta(503)=1;

% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;

%-------------------------depression-II/IIIRS-->II/IIIRS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
tfac = 0.00001;
UU = 0.3;

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
title('EPSP, II/IIIRS-->II/IIIRS');
xlabel('time [ms]');
ylim([-0.1,1]);
hold on;

%-------------------------depression-VRS-->VRS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 350.0;
tfac = 0.000001;
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
title('EPSP, VRS-->VRS');
xlabel('time [ms]');
ylim([-0.1,1]);

%-------------------------depression-VIRS-->VIRS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 150.0;
tfac = 0.000001;
UU = 0.15;

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
title('EPSP, VIRS-->VIRS');
xlabel('time [ms]');
ylim([-0.1,1.3]);
hold on;

%-------------------------depression-VRS-->II/IIIRS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
tfac = 0.00001;
UU = 0.4;

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
title('EPSP, VRS-->II/IIIRS');
xlabel('time [ms]');
ylim([-0.1,1]);


%-------------------------depression-II/IIIRS-->VRS-------------------------------

sd = zeros(3, totalSteps);

ti =3.0;
trec = 100.0;
tfac = 0.00001;
UU = 0.4;

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
title('EPSP, II/IIIRS-->VRS');
xlabel('time [ms]');
ylim([-0.1,1]);
hold on;

