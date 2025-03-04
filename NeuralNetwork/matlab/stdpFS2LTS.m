
endTime=200;
timestep = 0.1;
totalSteps = endTime / timestep;
%EPSP = epsp(0.1:0.1:endTime);
timeEPSP = 0.1:0.1:endTime;

delta = zeros(1, totalSteps);
%delta(1:500:4500)=1;
%delta(totalSteps*9/10:500:totalSteps) = 1;
%delta(1:500:20000)=1;
 delta(3:100:1500)=1;
% delta(16000:1:24000) = 1;
%delta(totalSteps*7/8:500:totalSteps) = 1;
%-------------------------depression-II/IIIFS-->II/IIILTS-------------------------------


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
title('IPSP,II/IIIFS-->II/IIILTS');
xlabel('time [ms]');
ylim([-1.5, 0.1]);
hold on;