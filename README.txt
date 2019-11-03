revise/improve:

loading 2K+ objects in memory including images is expensive, so, probably need to save them to db once loaded (e.g. batched - per page).
though will take longer because of adding db save/get but less memory expensive.

is existed db update is necessary (by remote id or name)?

