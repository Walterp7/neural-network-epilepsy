function [ P ] = Pmatr( dt, tfac, trec, ti)

P = [exp(-dt/tfac),0,0,0; 
    0, exp(-dt/trec), ti*(exp(-dt/trec)-exp(-dt/ti))/(ti-trec), 1-exp(-dt/trec);
    0,0,exp(-dt/ti),0];

end

