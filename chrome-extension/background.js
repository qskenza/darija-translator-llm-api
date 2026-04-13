chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: 'translate-to-darija',
    title: 'Translate to Darija',
    contexts: ['selection']
  });

  chrome.sidePanel
    .setPanelBehavior({ openPanelOnActionClick: true })
    .catch((error) => console.error(error));
});

chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId !== 'translate-to-darija') return;

  chrome.storage.local.set({
    selectedText: info.selectionText || ''
  });
});