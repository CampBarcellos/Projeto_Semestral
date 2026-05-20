const API = 'http://localhost:8080';
let usuarioLogado = null;
let livros = [];
let livroEditandoId = null;
let livroParaDeletarId = null;
const SPINES = ['📗','📘','📙','📕','📓','📔'];

/* ════════════════════════════════════
   UTILS
════════════════════════════════════ */
function showPage(id) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

function showSub(id) {
  document.querySelectorAll('.subpage').forEach(s => s.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

function toast(msg, type = 'ok') {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.className = 'show ' + type;
  setTimeout(() => { t.className = ''; }, 3000);
}

function showMsg(id, msg, type) {
  const el = document.getElementById(id);
  el.textContent = msg;
  el.className = 'msg ' + type;
}

function clearMsg(id) {
  document.getElementById(id).className = 'msg';
}

function spineFor(id) {
  return SPINES[String(id).charCodeAt(0) % SPINES.length];
}

/* ════════════════════════════════════
   CEP — ViaCEP
════════════════════════════════════ */
async function buscarCep(valor) {
  const cep = valor.replace(/\D/g, '');
  if (cep.length !== 8) return;
  try {
    const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
    const data = await res.json();
    if (data.erro) return showMsg('msg-cadastro', 'CEP não encontrado.', 'error');
    document.getElementById('cad-rua').value    = data.logradouro || '';
    document.getElementById('cad-cidade').value = data.localidade || '';
    document.getElementById('cad-estado').value = data.uf || '';
  } catch(e) {
    showMsg('msg-cadastro', 'Erro ao buscar CEP.', 'error');
  }
}

/* ════════════════════════════════════
   AUTH
════════════════════════════════════ */
function switchTab(tab) {
  document.querySelectorAll('.auth-tab').forEach((t, i) => {
    t.classList.toggle('active', (tab === 'login' && i === 0) || (tab === 'cadastro' && i === 1));
  });
  document.getElementById('form-login').classList.toggle('active', tab === 'login');
  document.getElementById('form-cadastro').classList.toggle('active', tab === 'cadastro');
  clearMsg('msg-login');
  clearMsg('msg-cadastro');
}

async function fazerLogin() {
  const email = document.getElementById('login-email').value.trim();
  const senha  = document.getElementById('login-senha').value;

  if (!email || !senha) return showMsg('msg-login', 'Preencha todos os campos.', 'error');

  try {
    const res = await fetch(`${API}/usuarios`);
    const usuarios = await res.json();
    const u = usuarios.find(x => x.email === email && x.senha === senha);
    if (!u) return showMsg('msg-login', 'E-mail ou senha incorretos.', 'error');

    usuarioLogado = u;
    sessionStorage.setItem('bib_usuario', JSON.stringify(u));
    await carregarLivros();
    entrarNoApp();
  } catch(e) {
    showMsg('msg-login', 'Erro ao conectar ao servidor.', 'error');
  }
}

async function fazerCadastro() {
  const nome   = document.getElementById('cad-nome').value.trim();
  const email  = document.getElementById('cad-email').value.trim();
  const senha  = document.getElementById('cad-senha').value;
  const cep    = document.getElementById('cad-cep').value.trim();
  const rua    = document.getElementById('cad-rua').value.trim();
  const cidade = document.getElementById('cad-cidade').value.trim();
  const estado = document.getElementById('cad-estado').value.trim();

  if (!nome || !email || !senha) return showMsg('msg-cadastro', 'Preencha nome, email e senha.', 'error');
  if (senha.length < 6) return showMsg('msg-cadastro', 'A senha deve ter pelo menos 6 caracteres.', 'error');
  if (!/\S+@\S+\.\S+/.test(email)) return showMsg('msg-cadastro', 'E-mail inválido.', 'error');

  try {
    const res = await fetch(`${API}/usuarios`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome, email, senha, cep, rua, cidade, estado })
    });
    if (!res.ok) return showMsg('msg-cadastro', 'Erro ao cadastrar.', 'error');
    showMsg('msg-cadastro', '✅ Conta criada! Faça login.', 'success');
    setTimeout(() => switchTab('login'), 1500);
  } catch(e) {
    showMsg('msg-cadastro', 'Erro ao conectar ao servidor.', 'error');
  }
}

function entrarNoApp() {
  document.getElementById('user-nome-header').textContent = usuarioLogado.nome;
  document.getElementById('user-avatar').textContent = usuarioLogado.nome[0].toUpperCase();
  renderizarLivros();
  showPage('page-app');
  showSub('sub-lista');
}

function sair() {
  sessionStorage.removeItem('bib_usuario');
  usuarioLogado = null;
  livros = [];
  document.getElementById('login-email').value = '';
  document.getElementById('login-senha').value = '';
  showPage('page-auth');
  switchTab('login');
}

/* ════════════════════════════════════
   LIVROS
════════════════════════════════════ */
async function carregarLivros() {
  try {
    const res = await fetch(`${API}/livros`);
    livros = await res.json();
  } catch(e) {
    livros = [];
  }
}

function irParaLista() {
  livroEditandoId = null;
  renderizarLivros();
  showSub('sub-lista');
}

function irParaForm(id = null) {
  livroEditandoId = id;
  clearMsg('msg-form');
  const titulo = document.getElementById('form-titulo');

  if (id) {
    const livro = livros.find(l => l.id === id);
    titulo.innerHTML = 'Editar <span>Livro</span>';
    document.getElementById('f-titulo').value  = livro.titulo;
    document.getElementById('f-autor').value   = livro.autor;
    document.getElementById('f-ano').value     = livro.ano;
    document.getElementById('f-paginas').value = livro.paginas;
  } else {
    titulo.innerHTML = 'Adicionar <span>Livro</span>';
    document.getElementById('f-titulo').value  = '';
    document.getElementById('f-autor').value   = '';
    document.getElementById('f-ano').value     = '';
    document.getElementById('f-paginas').value = '';
  }
  showSub('sub-form');
}

async function salvarLivro() {
  const titulo  = document.getElementById('f-titulo').value.trim();
  const autor   = document.getElementById('f-autor').value.trim();
  const ano     = parseInt(document.getElementById('f-ano').value);
  const paginas = parseInt(document.getElementById('f-paginas').value);

  if (!titulo || !autor) return showMsg('msg-form', 'Título e autor são obrigatórios.', 'error');
  if (isNaN(ano) || ano < 0 || ano > 2100) return showMsg('msg-form', 'Ano inválido.', 'error');
  if (isNaN(paginas) || paginas <= 0) return showMsg('msg-form', 'Número de páginas inválido.', 'error');

  try {
    if (livroEditandoId) {
      await fetch(`${API}/livros/${livroEditandoId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ titulo, autor, ano, paginas })
      });
      toast('✅ Livro atualizado!');
    } else {
      await fetch(`${API}/livros`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ titulo, autor, ano, paginas })
      });
      toast('✅ Livro adicionado!');
    }
    await carregarLivros();
    irParaLista();
  } catch(e) {
    showMsg('msg-form', 'Erro ao salvar livro.', 'error');
  }
}

function deletarLivro(id) {
  livroParaDeletarId = id;
  document.getElementById('modal-del').classList.add('open');
}

function fecharModal() {
  livroParaDeletarId = null;
  document.getElementById('modal-del').classList.remove('open');
}

async function confirmarDeletar() {
  try {
    await fetch(`${API}/livros/${livroParaDeletarId}`, { method: 'DELETE' });
    await carregarLivros();
    fecharModal();
    renderizarLivros();
    toast('🗑️ Livro removido.', 'err');
  } catch(e) {
    toast('Erro ao remover livro.', 'err');
  }
}

function filtrarLivros() {
  const q = document.getElementById('search-input').value.toLowerCase();
  renderizarLivros(q);
}

function renderizarLivros(filtro = '') {
  const grid  = document.getElementById('livros-grid');
  const stats = document.getElementById('stats-bar');

  const lista = filtro
    ? livros.filter(l =>
        l.titulo.toLowerCase().includes(filtro) ||
        l.autor.toLowerCase().includes(filtro))
    : livros;

  const totalPaginas = livros.reduce((s, l) => s + (l.paginas || 0), 0);

  stats.innerHTML = `
    <span class="stat-chip"><strong>${livros.length}</strong> livros</span>
    ${totalPaginas ? `<span class="stat-chip"><strong>${totalPaginas.toLocaleString()}</strong> páginas no total</span>` : ''}
    ${filtro ? `<span class="stat-chip"><strong>${lista.length}</strong> resultado(s) para "${filtro}"</span>` : ''}
  `;

  if (lista.length === 0) {
    grid.innerHTML = `
      <div class="empty-state" style="grid-column:1/-1">
        <div class="big-icon">${filtro ? '🔍' : '📭'}</div>
        <p>${filtro ? 'Nenhum livro encontrado para essa busca.' : 'Sua biblioteca está vazia. Adicione o primeiro livro!'}</p>
      </div>`;
    return;
  }

  grid.innerHTML = lista.map((l, i) => `
    <div class="livro-card" style="animation-delay:${i * 60}ms">
      <div class="livro-spine">${spineFor(l.id)}</div>
      <div class="livro-titulo">${l.titulo}</div>
      <div class="livro-autor">${l.autor}</div>
      <div class="livro-meta">
        <span class="badge">📅 ${l.ano}</span>
        <span class="badge">📄 ${l.paginas} págs.</span>
      </div>
      <div class="livro-actions">
        <button class="btn-edit" onclick="irParaForm(${l.id})">✏️ Editar</button>
        <button class="btn-del"  onclick="deletarLivro(${l.id})">🗑️ Remover</button>
      </div>
    </div>
  `).join('');
}

/* ════════════════════════════════════
   INIT
════════════════════════════════════ */
(function init() {
  const s = sessionStorage.getItem('bib_usuario');
  if (s) {
    usuarioLogado = JSON.parse(s);
    carregarLivros().then(() => entrarNoApp());
  }
})();