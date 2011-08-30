endTime=2500;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:1000:10020)=1;
% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;

%----------------------facilitation-IVRS--->IVLTS---------------------------------

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


plot(timeEPSP, resultRS2LTS);
title('EPSP, IVRS to IVLTS');
xlabel('time [ms]');
%ylim([-0.1,9]);

%----------------------facilitation-II/IIIRS--->II/IIILTS---------------------------------

sf = zeros(3, totalSteps);

ti =3.0;
trec = 150%20.0;
tfac = 200%900;%1000;
UU = 0.02%0.02;


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


plot(timeEPSP, resultRS2LTS);
title('EPSP, II/IIIRS--->II/IIILTS');
xlabel('time [ms]');
%ylim([-0.1,9]);