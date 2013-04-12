subplot(4,1,1) ;
bins = floor(max(dataIII(1:100,1))-min(dataIII(1:100,1)))
hist(dataIII(1:100,1), bins);

bins = floor(max(dataVI(1:35,1)) - min(dataVI(1:35,1)))
subplot(4,1,2) ;hist(dataIV(1:35,1), bins);

bins = floor(max(dataV(1:150,1))- min(dataV(1:150,1)))
subplot(4,1,3) ;hist(dataV(1:150,1), bins); 

bins = floor(max(dataVI(1:50,1)) - min(dataVI(1:50,1)))
subplot(4,1,4) ;hist(dataVI(1:50,1), bins);

figure

subplot(4,1,1) ;
bins = floor(max(dataIII(:,1))-min(dataIII(:,1)))
hist(dataIII(:,1), bins);

bins = floor(max(dataVI(:,1)) - min(dataVI(:,1)))
subplot(4,1,2) ;hist(dataIV(:,1), bins);

bins = floor(max(dataV(:,1))- min(dataV(:,1)))
subplot(4,1,3) ;hist(dataV(:,1), bins); 

bins = floor(max(dataVI(:,1)) - min(dataVI(:,1)))
subplot(4,1,4) ;hist(dataVI(:,1), bins);