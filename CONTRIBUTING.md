There are just a few small guidelines you need to follow.

## Branching rules

https://github.com/wisnukurniawan/Compose-ToDo/blob/main/doc/branching.md

## Code reviews

**The overall goal of a code review is to serve as a safety net for other people on our team and help them write better code, not to judge them or their code. When in doubt, assume that they have good
intention and BE NICE.**

### Goals:

- Catch bugs.
- Catch non-obvious consequences of an approach - will this PR make future code harder to secure or more buggy.
- For situations where things were coded without being discussed, a code review serves as a sanity check to make sure a correct approach is being taken.
- Point out places where a good approach or style was used. Code reviews are not a hatefest. Unless a PR is completely horrific there should be an equal number of good and bad points brought up.

### Mindset giving a Code Review:

Your primary goal as a reviewer is to serve as a safety net and keep bad code from being merged. The definition of “bad” is highly subjective, context dependent and will change with product time and
maturity.

When you find clear mistakes, take the time to note why you think they are mistakes.

If you see places where you don’t agree with an approach, speak up. However, also take the time to understand why the author made a certain choice. You should assume that the author made a good
decision based on what they knew in the moment. You probably have a different set of knowledge and see different outcomes. Dig into these. They might see things you don’t and vice versa.

Look for tricks, techniques, or idioms you can steal. Your teammates are smart folks. Chances are they have tricks that you can learn from. Make a point of letting them know.

### Mindset getting a Code Review:

Be considerate, your work will be used by other people, and you in turn will depend on the work of others. Any decision you take will affect users and colleagues, and you should take those
consequences into account when making decisions.

The reviewer is doing you a Solid. They are there to help you do the best work you can. The best of the best have coaches, editors and mentors. Your code reviewers should help you in the same way. In
situations where they are more experienced, this can be direct mentoring. In situations where they are more junior, they have a fresh pair of eyes that might get you to question deeply held
assumptions.

When a reviewer disagrees with an approach you took, seek to understand why. Different people have different perspectives on issues. They might know things or see consequences you didn’t. While they
might not have thought as deeply on the specific subject of the PR as you, they likewise probably are thinking about the impacts of the PR on areas you might not be paying attention to.

Disagreements, both social and technical, happen all the time. If someone disagrees on your PR, be especially patient. Dig into why they think the PR is flawed. Approach the conversation with an
intent of making the PR better, not defending your approach. You get no points for being a better debater, but you do get points for shipping better code and a better product, no matter where the
inspiration or ideas came from.

### Attribution & Acknowledgements:

- [Spotify](https://github.com/spotify/code-of-conduct/blob/master/code-of-conduct.md)
- [Metabase](https://github.com/metabase/metabase/blob/master/docs/code-reviews.md)
- [Google](https://google.github.io/eng-practices/review/reviewer/)
