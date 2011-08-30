endTime=1000;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:1500:8020)=1;
% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;

%----------------------facilitation-LTS2LTS---------------------------------

sf = zeros(3, totalSteps);

ti =3.0;
trec = 600;
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

plot(timeEPSP, resultLTS2LTS);
title('IPSP,  LTS to LTS');
xlabel('time [ms]');