My chain of thought explanation (almost the same what I wrote in the commit messages, but more detailed):
1. First I migrated the solution to the SpringBoot project - it may be an overkill, but since there is a time range I choose it as the fastest way to provide REST api.
2. Next I changed the Vehicle Interface to enum - I don't see any reason to have it as an interface nor use a polymorphism.
3. Next I created proper rest controller to make it buildable.
4. I spotted two bugs in time ranges: 08:30 - 14:59 and 15:30 - 16:59 - now we can go for testing (unit and usage). I have quite a lot of ideas to improve the code base, but first make it "workable".
5. Small refactor for toll-free check. Separated to TollFeeService to separate responsibility and first focus on calculating single fee.
6. Changed Date to LocalDateTime.
7. Optimized fee calculation and checking toll-free day.
8. Prepared data as collections to be easy replaceable by sth from DB - if I'd have extra 1h or two I'd create H2 store for it.
