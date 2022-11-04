# helix-refx-playground
Personal Helix+Refx experiments for a eventual starterkit repo.  
Heavily inspired by [vloth/reagent-boilerplate](https://github.com/vloth/reagent-boilerplate) and [re-frame/app-structure](https://github.com/day8/re-frame/blob/master/docs/App-Structure.md)

## Commands

### Watch
Start shadow-cljs watching and serving main in [`localhost:8000`](http://localhost:8000) and tests in [`localhost:8100`](http://localhost:8100)
```bash
npm run watch
```

### Watch App
Start shadow-cljs watching and serving main in [`localhost:8000`](http://localhost:8000)
```bash
npm run watch:app
```

### Tests
Start shadow-cljs watching and serving tests in [`localhost:8100`](http://localhost:8100)
```bash
npm run watch:tests
```

Run **Karma** tests targeted for running CI tests with *Headless Chromium Driver*
```bash
npm run ci:test
```

### Release
Build the release package to production deploy
```bash
npm run release
```

## MSW
I decided to start the app (even on development) with [MSW](https://mswjs.io/) turned off.  

To enable it you need an active repl connection with the project and and evaluate
the start expression on the block comment at the end of the following file: [src/dev/core.cljs](src/dev/core.cljs)
```clj
(p/do (browser/start!)
 (.reload js/location))
```

## Demo 
You can check this sample running in your browser here:  
http://rafael.delboni.cc/helix-refx-playground
